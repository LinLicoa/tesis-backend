package com.application.webapi.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSchemaFixer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Checking database schema for precision issues...");
        
        // List of columns to fix in 'evaluaciones' table
        List<String> columns = List.of(
            "ansiedad_t", "ansiedad_i", "ansiedad_f",
            "depresion_t", "depresion_i", "depresion_f",
            "estres_t", "estres_i", "estres_f"
        );

        for (String col : columns) {
            fixColumnPrecision(col);
        }
        
        log.info("Database schema check completed.");
    }

    private void fixColumnPrecision(String columnName) {
        try {
            // Check if column exists and get its type/precision?
            // Simplified: Try to find constraint and drop it, then alter.
            
            // 1. Find Check Constraint Name for this column
            String findConstraintSql = "SELECT name FROM sys.check_constraints WHERE parent_object_id = OBJECT_ID('evaluaciones') AND parent_column_id = COLUMNPROPERTY(OBJECT_ID('evaluaciones'), ?, 'ColumnId')";
            
            try {
                List<String> constraints = jdbcTemplate.query(findConstraintSql, (rs, rowNum) -> rs.getString("name"), columnName);
                
                for (String constraintName : constraints) {
                    log.info("Dropping constraint {} on column {}", constraintName, columnName);
                    jdbcTemplate.execute("ALTER TABLE evaluaciones DROP CONSTRAINT " + constraintName);
                }
            } catch (Exception e) {
                log.warn("Could not find/drop constraint for {}: {}", columnName, e.getMessage());
            }

            // 2. Alter Column
            // We blindly try to alter. If it's already correct, it's fine.
            // Note: We use numeric(20,18) as per Entity definition
            log.info("Altering column {} to numeric(20,18)", columnName);
            jdbcTemplate.execute("ALTER TABLE evaluaciones ALTER COLUMN " + columnName + " numeric(20, 18)");
            
        } catch (Exception e) {
            log.error("Failed to fix column {}: {}", columnName, e.getMessage());
        }
    }
}
