import React, { useEffect, useState, useRef } from 'react';
import { getEventStatistics } from '../../actions/eventActions';
import { 
    Chart as ChartJS,
    ArcElement,
    Tooltip,
    Legend,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    RadialLinearScale,
    PointElement,
    LineElement
} from 'chart.js';
import { Doughnut, Bar, PolarArea, Chart } from 'react-chartjs-2';
import randomColor from 'randomcolor';

ChartJS.register(
    ArcElement,
    Tooltip,
    Legend,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    RadialLinearScale,
    PointElement,
    LineElement
);

const EventStatistics = () => {
    const chartRef = useRef(null);
    const [localEventStatistics, setLocalEventStatistics] = useState({
        averages: null,
        numberOfEventsByCategory: null,
        averageCostOfEventsByCategory: null,
        averageAgeLimitOfEventsByCategory: null,
        numberOfEventsByCategoryTimeRanges: null
    });

    useEffect(() => {
        const chart = chartRef.current;
        
        // if (!chart) {
        //     return;
        // }

        const fetchEventStatistics = async () => {
            try {
                const eventStatistics = await getEventStatistics();

                console.log(eventStatistics);
                const numberOfEventsByCategoryColors = Object.keys(eventStatistics.numberOfEventsByCategory).map(x => randomColor({ format: 'rgba' }));

                // Get all categories
                const categories = [];
                Object.keys(eventStatistics.numberOfEventsByCategoryTimeRanges).forEach(timeRange => {
                    let timeRangeVal = eventStatistics.numberOfEventsByCategoryTimeRanges[timeRange];
                    Object.keys(timeRangeVal).forEach(category => {
                        if (!categories.includes(category)) {
                            categories.push(category);
                        }
                    });
                });

                const numberOfEventsByCategoryTimeRangesDatasets = categories.map(category => {
                    const dataset = { label: category };
                    dataset['data'] = Object.keys(eventStatistics.numberOfEventsByCategoryTimeRanges).map(timeRange => {
                        let timeRangeVal = eventStatistics.numberOfEventsByCategoryTimeRanges[timeRange];

                        if (category in timeRangeVal) {
                            return timeRangeVal[category];
                        } else {
                            return 0;
                        }
                    });

                    return dataset;
                })

                console.log(numberOfEventsByCategoryTimeRangesDatasets);
                console.log(Object.keys(eventStatistics.numberOfEventsByCategoryTimeRanges));

                setLocalEventStatistics({
                    averages: {
                        labels: ['Cost', 'Age Limit'],
                        datasets: [
                            {
                                label: 'Averages',
                                data: [eventStatistics.averageCostForEvents, eventStatistics.averageAgeLimitForEvents],
                                backgroundColor: randomColor({ format: 'rgba', alpha: 0.5 }),
                                borderWidth: 1
                            }
                        ]
                    },
                    numberOfEventsByCategory: {
                        data: Object.keys(eventStatistics.numberOfEventsByCategory).map(x => eventStatistics.numberOfEventsByCategory[x]),
                        labels: Object.keys(eventStatistics.numberOfEventsByCategory),
                        bgColor: numberOfEventsByCategoryColors.map(color => color.substring(0, color.lastIndexOf(',') + 1) + ' 0.2)'),
                        borderColor: numberOfEventsByCategoryColors.map(color => color.substring(0, color.lastIndexOf(',') + 1) + ' 1)')
                    },
                    averageCostOfEventsByCategory: {
                        labels: Object.keys(eventStatistics.averageCostOfEventsByCategory),
                        datasets: [{
                            label: 'Average Cost of Events By Category',
                            data: Object.keys(eventStatistics.averageCostOfEventsByCategory).map(x => eventStatistics.averageCostOfEventsByCategory[x]),
                            backgroundColor: Object.keys(eventStatistics.averageCostOfEventsByCategory).map(x => randomColor({ format: 'rgba', alpha: 0.5 })),
                            borderWidth: 1                            
                        }]
                    },
                    averageAgeLimitOfEventsByCategory: {
                        labels: Object.keys(eventStatistics.averageAgeLimitOfEventsByCategory),
                        datasets: [{
                            label: 'Average Age Limit of Events By Category',
                            data: Object.keys(eventStatistics.averageAgeLimitOfEventsByCategory).map(x => eventStatistics.averageAgeLimitOfEventsByCategory[x]),
                            backgroundColor: Object.keys(eventStatistics.averageAgeLimitOfEventsByCategory).map(x => randomColor({ format: 'rgba', alpha: 0.5 })),
                            borderWidth: 1
                        }]
                    },
                    numberOfEventsByCategoryTimeRanges: {
                        labels: Object.keys(eventStatistics.numberOfEventsByCategoryTimeRanges),
                        // datasets: numberOfEventsByCategoryTimeRangesDatasets.map(dataset => ({
                        //     ...dataset,
                        //     borderColor: createGradient(chart?.ctx, chart?.chartArea)
                        // }))
                        datasets: numberOfEventsByCategoryTimeRangesDatasets
                    }
                })
            } catch (err) {
                console.error(err);
            }
        };

        fetchEventStatistics();
    }, []);

    /**
     * Gradient function example is inspired and 
     * credited to the react-cartjs-2 documentation page:
     * https://react-chartjs-2.js.org/examples/gradient-chart
     * 
     * @param {CanvasRenderingContext2D} ctx 
     * @param {ChartArea} area 
     * @returns gradient
     */
    const createGradient = (ctx, area) => {
        const colorStart = randomColor({ format: 'rgb' });
        const colorMid = randomColor({ format: 'rgb' });
        const colorEnd = randomColor({ format: 'rgb' });
        
        const gradient = ctx?.createLinearGradient(0, area.bottom, 0, area.top);
        
        gradient.addColorStop(0, colorStart);
        gradient.addColorStop(0.5, colorMid);
        gradient.addColorStop(1, colorEnd);
        
        return gradient;
    }

    return (
        <>
            <h1>Event Statistics</h1>
            {
                localEventStatistics && localEventStatistics.averages &&
                <Bar
                    options={{
                        responsive: true,
                        plugins: {
                        legend: {
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: 'Chart.js Bar Chart',
                        },
                        }
                    }}
                    data={localEventStatistics?.averages}
                />
            }
            <Doughnut data={{
                    labels: localEventStatistics?.numberOfEventsByCategory?.labels,
                    datasets: [{
                        label: 'Number of Events By Category',
                        data: localEventStatistics?.numberOfEventsByCategory?.data,
                        backgroundColor: localEventStatistics?.numberOfEventsByCategory?.bgColor,
                        borderColor: localEventStatistics?.numberOfEventsByCategory?.borderColor,
                        borderWidth: 1,
                    }]
                }}
            />
            {
                localEventStatistics && localEventStatistics.averageCostOfEventsByCategory &&
                    <PolarArea data={localEventStatistics?.averageCostOfEventsByCategory} />
            }
            {
                localEventStatistics && localEventStatistics.averageAgeLimitOfEventsByCategory &&
                    <PolarArea data={localEventStatistics?.averageAgeLimitOfEventsByCategory} />
            }
            {
                localEventStatistics && localEventStatistics.numberOfEventsByCategoryTimeRanges &&
                    <Chart
                        ref={chartRef}
                        type='line'
                        data={{
                            ...localEventStatistics.numberOfEventsByCategoryTimeRanges,
                            datasets: localEventStatistics.numberOfEventsByCategoryTimeRanges.datasets.map(dataset => ({
                                ...dataset,
                                borderColor: createGradient(chartRef?.current?.ctx, chartRef?.current?.chartArea)
                            }))
                        }}
                    />
            }
        </>
    );
};

export default EventStatistics;