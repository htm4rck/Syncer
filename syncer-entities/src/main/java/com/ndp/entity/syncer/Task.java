package com.ndp.entity.syncer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@NamedQuery(name = "Task.findCode", query = "SELECT t FROM Task t WHERE t.code = :code")
@NamedQuery(name = "Task.findBySourceCode", query = "SELECT t FROM Task t WHERE t.sourceCode = :sourceCode")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sync_task", nullable = false, columnDefinition = "BIGINT COMMENT 'Identificador único de la tarea'")
    public Long id;

    @Column(name = "tx_group", nullable = false, columnDefinition = "VARCHAR(32) COMMENT 'Grupo al que pertenece la tarea'")
    public String group;
    @Column(name = "tx_source_service", nullable = true, columnDefinition = "VARCHAR(64) COMMENT 'Ruta de la fuente'")
    public String sourceService;

    @Column(name = "tx_source_code", nullable = true, columnDefinition = "VARCHAR(32) COMMENT 'Código de la fuente'")
    public String sourceCode;

    @Column(name = "tx_source_path", nullable = true, columnDefinition = "VARCHAR(64) COMMENT 'Ruta de la fuente'")
    public String sourcePath;

    @Column(name = "tx_source_name", nullable = true, columnDefinition = "VARCHAR(64) COMMENT 'Nombre de la fuente'")
    public String sourceName;

    @Column(name = "tx_destination_code", nullable = true, columnDefinition = "VARCHAR(64) COMMENT 'Código de destino'")
    public String destinationCode;

    @Column(name = "tx_destination_path", nullable = true, columnDefinition = "VARCHAR(128) COMMENT 'Ruta de destino'")
    public String destinationPath;

    @Column(name = "tx_destination_name", nullable = true, columnDefinition = "VARCHAR(32) COMMENT 'Nombre de destino'")
    public String destinationName;

    @Column(name = "tx_code", nullable = false, length = 32, columnDefinition = "VARCHAR(128) COMMENT 'Código de la tarea'")
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

    @Column(name = "bl_bulk_add", nullable = true, columnDefinition = "CHAR(1) COMMENT 'Indicador de carga masiva Y | N'")
    public String bulkAdd = "N";

    @Column(name = "tx_migration_type", nullable = true, columnDefinition = "VARCHAR(16) COMMENT 'Tipo de migración de la tarea'")
    public String migrationType = "";
}
