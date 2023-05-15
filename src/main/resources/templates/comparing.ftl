<#import "parts/common.ftl" as c>

<@c.page>
    <div class="card-group" id="message-list">
        <#if projects??>
            <#assign counter = 1>
            <#list projects as prj>
                <div class="card">
                    <div class="chart-container">
                        <canvas id="sentimentPie${counter}" width="200" height="200"></canvas>
                        <canvas id="sentimentChart${counter}" width="600" height="200"></canvas>
                    </div>

                    <div class="chart-container2">
                        <canvas id="mentionsChart${counter}" width="400" height="200"></canvas>
                        <canvas id="reachChart${counter}" width="400" height="200"></canvas>
                    </div>


                    <script>
                        const sentimentPieData${counter} = ${prj.project.socialMediaPlatform.analyseData.sentimentPieGraph};
                    </script>

                    <script>
                        const sentimentChartData${counter} = ${prj.project.socialMediaPlatform.analyseData.sentimentDataChart}
                    </script>

                    <script>
                        const mentionChartData${counter} = ${prj.project.socialMediaPlatform.analyseData.totalMentionsCountChart}
                    </script>

                    <script>
                        const reachChartData${counter} = ${prj.project.socialMediaPlatform.analyseData.reachAnalysis}
                    </script>

                    <script>
                        const words${counter} = ${prj.project.socialMediaPlatform.analyseData.wordCloudGeneration};
                    </script>

                    <div id="word-cloud${counter}"></div>

                    <div class="card-footer text-muted container">
                        <form action="/comparison-delete/${prj.project.id}" method="post">
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <input class="col btn btn-outline-secondary" type="submit" value="Remove from comparison">
                        </form>
                    </div>

                    <script>
                        (async function () {
                            const data = sentimentPieData${counter};

                            new Chart(
                                document.getElementById('sentimentPie${counter}'),
                                {
                                    type: 'pie',
                                    data: {
                                        labels: data.map(row => row.sentiment),
                                        datasets: [
                                            {
                                                label: 'Percentage',
                                                data: data.map(row => row.percentage),
                                                backgroundColor: data.map(row => {
                                                    if (row.sentiment === 'Positive') {
                                                        return 'rgb(21,197,11)';
                                                    } else if (row.sentiment === 'Negative') {
                                                        return 'rgb(255,0,26)';
                                                    } else if (row.sentiment === 'Neutral') {
                                                        return 'rgb(175,175,175)';
                                                    } else if (row.sentiment === 'Very positive') {
                                                        return 'rgb(15,82,0)';
                                                    } else {
                                                        return 'rgb(90,1,0)';
                                                    }
                                                }),
                                                hoverOffset: 4
                                            }
                                        ]
                                    },
                                    plugins: [ChartDataLabels],
                                    options: {
                                        plugins: {
                                            datalabels: {
                                                formatter: (value, context) => {
                                                    return value + '%';
                                                },
                                                color: '#fff',
                                                font: {
                                                    size: '23'
                                                }
                                            }
                                        }
                                    }
                                }
                            );
                        })();
                    </script>
                    <script>(async function () {
                            const data = sentimentChartData${counter};

                            new Chart(
                                document.getElementById('sentimentChart${counter}'),
                                {
                                    type: 'line',
                                    data: {
                                        labels: data.map(row => row.date),
                                        datasets: [
                                            {
                                                label: 'Positive',
                                                data: data.map(row => row.sentiment.positive),
                                                fill: false,
                                                borderColor: 'rgb(21,197,11)',
                                                tension: 0.3
                                            },
                                            {
                                                label: 'Negative',
                                                data: data.map(row => row.sentiment.negative),
                                                fill: false,
                                                borderColor: 'rgb(255,0,0)',
                                                tension: 0.3
                                            }
                                        ]
                                    }
                                }
                            );
                        })();</script>
                    <script>
                        (async function () {
                            const data = mentionChartData${counter};

                            new Chart(
                                document.getElementById('mentionsChart${counter}'),
                                {
                                    type: 'line',
                                    data: {
                                        labels: data.map(row => row.date),
                                        datasets: [
                                            {
                                                label: 'Mentions count',
                                                data: data.map(row => row.mentionsCount),
                                                fill: true,
                                                borderColor: 'rgb(61,81,161)',
                                                tension: 0.3
                                            },
                                            // {
                                            //     label: 'Negative',
                                            //     data: data.map(row => row.sentiment.negative),
                                            //     fill: false,
                                            //     borderColor: 'rgb(255,0,0)',
                                            //     tension: 0.3
                                            // }
                                        ]
                                    }
                                }
                            );
                        })();</script>
                    <script>
                        (async function () {
                            const data = reachChartData${counter};

                            new Chart(
                                document.getElementById('reachChart${counter}'),
                                {
                                    type: 'line',
                                    data: {
                                        labels: data.map(row => row.date),
                                        datasets: [
                                            {
                                                label: 'Reach count',
                                                data: data.map(row => row.mentionsCount),
                                                fill: true,
                                                borderColor: 'rgb(0,170,84)',
                                                tension: 0.3
                                            }
                                        ]
                                    }
                                }
                            );
                        })();
                    </script>
                    <script>
                        const width${counter} = 600;
                        const height${counter} = 500;

                        const svg${counter} = d3.select("#word-cloud${counter}")
                            .append("svg")
                            .attr("width", width${counter})
                            .attr("height", height${counter});

                        const layout${counter} = d3.layout.cloud()
                            .size([width${counter}, height${counter}])
                            .words(words${counter})
                            .padding(5)
                            .rotate(function () {
                                return ~~(Math.random() * 2) * 90;
                            })
                            .fontSize(function (d) {
                                return d.size;
                            })
                            .on("end", draw);

                        layout${counter}.start();

                        function draw(words) {
                            svg${counter}.append("g")
                                .attr("transform", "translate(" + layout${counter}.size()[0] / 2 + "," + layout${counter}.size()[1] / 2 + ")")
                                .selectAll("text")
                                .data(words)
                                .enter().append("text")
                                .style("font-size", function (d) {
                                    return d.size + "px";
                                })
                                .style("font-family", "Impact")
                                .style("fill", function (d, i) {
                                    return d3.schemeCategory10[i % 10];
                                })
                                .attr("text-anchor", "middle")
                                .attr("transform", function (d) {
                                    return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                                })
                                .text(function (d) {
                                    return d.text;
                                });
                        }
                    </script>

                    <#assign counter = counter + 1>
                </div>
            </#list>
        </#if>
    </div>
</@c.page>