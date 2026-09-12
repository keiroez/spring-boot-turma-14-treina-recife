package com.exemplo.gestao.model;

import com.exemplo.gestao.dto.ProjetoResponse;
import com.exemplo.gestao.model.enums.StatusProjeto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_projeto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_responsavel", nullable = false)
    private Usuario responsavel;

    public ProjetoResponse toDTO() {
        return new ProjetoResponse(
                this.id,
                this.nome,
                this.descricao,
                this.dataInicio,
                this.dataConclusao,
                this.status,
                this.responsavel != null ? this.responsavel.getId() : null,
                this.responsavel != null ? this.responsavel.getNome() : null
        );
    }
}
