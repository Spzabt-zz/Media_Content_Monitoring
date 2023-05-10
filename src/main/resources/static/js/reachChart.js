(async function () {
    const data = reachChartData;

    new Chart(
        document.getElementById('reachChart'),
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