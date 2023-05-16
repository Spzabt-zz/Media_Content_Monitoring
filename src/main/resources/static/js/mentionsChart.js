(async function () {
    const data = mentionChartData;

    new Chart(
        document.getElementById('mentionsChart'),
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
            },
            plugins: [ChartDataLabels],
            options: {
                plugins: {
                    datalabels: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Mentions count throughout the time'
                    }
                }
            }
        }
    );
})();