(async function () {
    const data = mentionsBysourceCount;

    new Chart(
        document.getElementById('mentionsPie'),
        {
            type: 'pie',
            data: {
                labels: data.map(row => row.source),
                datasets: [
                    {
                        label: 'Percentage',
                        data: data.map(row => row.countOfMentions),
                        backgroundColor: data.map(row => {
                            if (row.source === 'Reddit') {
                                return 'rgb(255,161,0)';
                            } else if (row.source === 'Twitter') {
                                return 'rgb(61,81,161)';
                            } else {
                                return 'rgb(255,12,0)';
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