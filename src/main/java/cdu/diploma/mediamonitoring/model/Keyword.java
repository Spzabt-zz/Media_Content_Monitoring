package cdu.diploma.mediamonitoring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "keyword")
@Data
@EqualsAndHashCode(of = {"id"})
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String keywords;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Project project;
}
