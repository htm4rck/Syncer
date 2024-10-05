package com.ndp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "t_task")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sync_task", nullable = false, columnDefinition = "BIGINT COMMENT 'Identificador único de la tarea'")
    public Long id;

    @Column(name = "tx_group", nullable = false, columnDefinition = "VARCHAR(32) COMMENT 'Grupo al que pertenece la tarea'")
    public String group;

    @Column(name = "tx_source_code", nullable = true, columnDefinition = "VARCHAR(32) COMMENT 'Código de la fuente'")
    public String sourceCode;

    @Column(name = "tx_source_path", nullable = true, columnDefinition = "VARCHAR(64) COMMENT 'Ruta de la fuente'")
    public String sourcePath;

    @Column(name = "tx_source_name", nullable = true, columnDefinition = "VARCHAR(64) COMMENT 'Nombre de la fuente'")
    public String sourceName;

    @Column(name = "tx_destination_code", nullable = true, columnDefinition = "VARCHAR(32) COMMENT 'Código de destino'")
    public String destinationCode;

    @Column(name = "tx_destination_path", nullable = true, columnDefinition = "VARCHAR(128) COMMENT 'Ruta de destino'")
    public String destinationPath;

    @Column(name = "tx_destination_name", nullable = true, columnDefinition = "VARCHAR(32) COMMENT 'Nombre de destino'")
    public String destinationName;

    @Column(name = "tx_code", nullable = false, length = 32, columnDefinition = "VARCHAR(32) COMMENT 'Código de la tarea'")
    public String code;

    @Column(name = "tx_name", nullable = false, length = 64, columnDefinition = "VARCHAR(64) COMMENT 'Nombre de la tarea'")
    public String name;

    @Column(name = "tx_app", nullable = false, length = 64, columnDefinition = "VARCHAR(64) COMMENT 'Aplicación relacionada con la tarea'")
    public String app;

    @Column(name = "tx_data", columnDefinition = "TEXT COMMENT 'Datos adicionales de la tarea'")
    public String data;

    @Column(name = "nu_order_execution", nullable = false, columnDefinition = "INT COMMENT 'Orden de ejecución de la tarea'")
    public int orderExecution;

    @Column(name = "tx_status_operation", nullable = false, columnDefinition = "VARCHAR(8) COMMENT 'Estado de la operación de la tarea'")
    public String statusOperation;

    @Column(name = "tx_status", nullable = false, columnDefinition = "VARCHAR(8) COMMENT 'Estado actual de la tarea'")
    public String status;

    @Column(name = "bl_bulk_add", nullable = false, columnDefinition = "CHAR(1) COMMENT 'Indicador de carga masiva Y | N'")
    public String bulkAdd;

    @Column(name = "tx_migration_type", nullable = false, columnDefinition = "VARCHAR(16) COMMENT 'Tipo de migración de la tarea'")
    public String migrationType;
}
