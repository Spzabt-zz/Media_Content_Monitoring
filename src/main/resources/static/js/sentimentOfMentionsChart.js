(async function () {
    const data = sentimentChartData;

    new Chart(
        document.getElementById('sentimentChart'),
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
            },
            plugins: [ChartDataLabels],
            options: {
                plugins: {
                    datalabels: {
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Sentiment count throughout the time'
                    }
                }
            }
        }
    );
})();