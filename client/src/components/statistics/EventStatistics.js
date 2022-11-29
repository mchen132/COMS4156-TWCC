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
import { Link } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import { getAuthInformation } from '../../utils/authUtil';
import '../../styles/eventStatistics.css';

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
        totalNumberOfEvents: null,
        averages: null,
        numberOfEventsByCategory: null,
        averageCostOfEventsByCategory: null,
        averageAgeLimitOfEventsByCategory: null,
        numberOfEventsByCategoryTimeRanges: null
    });

    const setChartOptions = (title) => ({
        responsive: true,
        plugins: {
            legend: {
                position: 'top'
            },
            title: {
                display: true,
                text: title
            }
        }
    });

    useEffect(() => {
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

                setLocalEventStatistics({
                    totalNumberOfEvents: eventStatistics.totalNumberOfEvents,
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
            <div className='events-statistics-header'>
                {
                        getAuthInformation('token')
                            ? <>
                                <h1>Event Statistics</h1>
                                <h2>Welcome {getAuthInformation('username')}!</h2>
                                {
                                    localEventStatistics && localEventStatistics.totalNumberOfEvents &&
                                    <h5>{`Total Number of Events: ${localEventStatistics?.totalNumberOfEvents}`}</h5>
                                }
                            </>
                            : <>                        
                                <h2>Login to view events</h2>
                                <Link to="/login"><Button variant='primary'>Login</Button></Link>
                            </>
                }
            </div>
            <div className='event-statistics-container'>
                <div className='event-statistics-chart'>
                    {
                        localEventStatistics && localEventStatistics.averages &&
                        <Bar
                            options={setChartOptions('Averages')}
                            data={localEventStatistics?.averages}
                        />
                    }
                </div>
                <div className='event-statistics-chart'>
                    <Doughnut
                        options={setChartOptions('Number of Events By Category')} 
                        data={{
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
                </div>
                <div className='event-statistics-chart'>
                    {
                        localEventStatistics && localEventStatistics.averageCostOfEventsByCategory &&
                            <PolarArea
                                options={setChartOptions('Average Cost of Events By Category')} 
                                data={localEventStatistics?.averageCostOfEventsByCategory}
                            />
                    }
                </div>
                <div className='event-statistics-chart'>
                    {
                        localEventStatistics && localEventStatistics.averageAgeLimitOfEventsByCategory &&
                            <PolarArea
                                options={setChartOptions('Average Age Limit of Events By Category')} 
                                data={localEventStatistics?.averageAgeLimitOfEventsByCategory}
                            />
                    }            
                </div>
                <div className='event-statistics-chart'>
                    <Chart
                        options={setChartOptions('Number of Events By Category within Time Ranges')} 
                        ref={chartRef}
                        type='line'
                        data={localEventStatistics && localEventStatistics.numberOfEventsByCategoryTimeRanges
                            ? {
                            ...localEventStatistics.numberOfEventsByCategoryTimeRanges,
                            datasets: localEventStatistics.numberOfEventsByCategoryTimeRanges.datasets.map(dataset => ({
                                ...dataset,
                                borderColor: createGradient(chartRef?.current?.ctx, chartRef?.current?.chartArea)
                            }))}
                            : {
                                labels: [],
                                datasets: []
                            } 
                        }
                    />
                </div>
            </div>
        </>
    );
};

export default EventStatistics;