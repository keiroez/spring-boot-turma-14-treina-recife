package com.exemplo.gestao.model;

import com.exemplo.gestao.dto.TarefaResponse;
import com.exemplo.gestao.model.enums.Prioridade;
import com.exemplo.gestao.model.enums.StatusTarefa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tb_tarefa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    @Column(name = "data_conclusao")
    private LocalDate dataConclusao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @ManyToOne
    @JoinColumn(name = "id_projeto")
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public TarefaResponse toDTO() {
        return new TarefaResponse(
                this.id,
                this.titulo,
                this.descricao,
                this.dataCriacao,
                this.dataConclusao,
                this.prioridade,
                this.status,
                this.projeto != null ? this.projeto.getId() : null,
                this.usuario != null ? this.usuario.getId() : null
        );
    }
}
