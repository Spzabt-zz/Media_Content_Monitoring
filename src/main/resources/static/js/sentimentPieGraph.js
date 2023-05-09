(async function () {
    const data = sentimentPieData;

    new Chart(
        document.getElementById('sentimentPie'),
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