package cdu.diploma.mediamonitoring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "analyse_data")
@Data
@EqualsAndHashCode(of = {"id"})
public class AnalyseData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(mappedBy = "analyseData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SocialMediaPlatform socialMediaPlatform;

    public String sentimentDataChart;
    public String sentimentPieGraph;
    public String totalMentionsCountChart;
    public String wordCloudGeneration;
    public String reachAnalysis;

    public AnalyseData() {

    }
}
