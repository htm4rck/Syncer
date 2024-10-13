package com.ndp.entity.queue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.NamedQuery;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Queue.findCode", query = "SELECT q FROM Queue q WHERE q.code = :code")
@NamedQuery(name = "Queue.findUID", query = "SELECT q FROM Queue q WHERE q.uid = :uid")
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_queue;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private String object;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String status;
    @Column
    private Integer attempts;
}
