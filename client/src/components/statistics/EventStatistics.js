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
import ToggleButtonGroup from 'react-bootstrap/ToggleButtonGroup';
import ToggleButton from 'react-bootstrap/ToggleButton';
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

const EventStatisticsToggle = {
    ALL: 'all-event-statistics',
    INDIVIDUAL: 'individual-event-statistics'
};

const EventStatistics = () => {
    const chartRef = useRef(null);
    const [localAllEventStatistics, setLocalAllEventStatistics] = useState({
        totalNumberOfEvents: null,
        averages: null,
        numberOfEventsByCategory: null,
        averageCostOfEventsByCategory: null,
        averageAgeLimitOfEventsByCategory: null,
        numberOfEventsByCategoryTimeRanges: null
    });
    const [localIndividualEventStatistics, setLocalIndividualEventStatistics] = useState({
        totalNumberOfEvents: null,
        averages: null,
        numberOfEventsByCategory: null,
        averageCostOfEventsByCategory: null,
        averageAgeLimitOfEventsByCategory: null,
        numberOfEventsByCategoryTimeRanges: null
    });
    const [eventStatisticsToggleValue, setEventStatisticsToggleValue] = useState(EventStatisticsToggle.ALL); 

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

    const formatAndSetEventStatistics = (eventStatistics, isIndividualEventStats) => {
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

        const generatedEventStatistics = {
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
        };

        isIndividualEventStats
            ? setLocalIndividualEventStatistics(generatedEventStatistics)
            : setLocalAllEventStatistics(generatedEventStatistics)
    };

    useEffect(() => {
        const fetchEventStatistics = async (hostId) => {
            try {
                const eventStatistics = await getEventStatistics(hostId);

                console.log(eventStatistics);
                formatAndSetEventStatistics(eventStatistics, hostId ? true : false);
            } catch (err) {
                console.error(err);
            }
        };

        fetchEventStatistics();
        fetchEventStatistics(getAuthInformation('userId'));
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

    /**
     * Gets the conditional eventStatistic React state
     * depending on current eventStatisticsToggleValue view.
     * 
     * @returns the conditional eventStatistic React state
     */
    const getCurrentEventStatisticsToDisplay = () => (
        eventStatisticsToggleValue === EventStatisticsToggle.ALL
            ? localAllEventStatistics
            : localIndividualEventStatistics
    );

    return (
        <>
            <div className='events-statistics-header'>
                {
                        getAuthInformation('token')
                            ? <>
                                <h1>Event Statistics</h1>
                                <h2>Welcome {getAuthInformation('username')}!</h2>
                                <ToggleButtonGroup
                                    name="event-statistics-radio"
                                    type="radio"
                                    value={eventStatisticsToggleValue}
                                    className="mb-2"
                                    onChange={setEventStatisticsToggleValue}
                                >
                                    <ToggleButton
                                        id="tbg-check-1"
                                        name={`${EventStatisticsToggle.ALL}-radio`}
                                        value={EventStatisticsToggle.ALL}
                                    >
                                        All Event Statistics
                                    </ToggleButton>
                                    <ToggleButton
                                        id="tbg-check-2"
                                        name={`${EventStatisticsToggle.INDIVIDUAL}-radio`}
                                        value={EventStatisticsToggle.INDIVIDUAL}
                                    >
                                        Individual Client ({getAuthInformation('username')}) Statistics
                                    </ToggleButton>                                                               
                                </ToggleButtonGroup>
                                {
                                    getCurrentEventStatisticsToDisplay() && getCurrentEventStatisticsToDisplay().totalNumberOfEvents &&
                                    <h5>{`Total Number of Events: ${getCurrentEventStatisticsToDisplay()?.totalNumberOfEvents}`}</h5>
                                }
                            </>
                            : <>                        
                                <h2>Login to view event statistics</h2>
                                <Link to="/login"><Button variant='primary'>Login</Button></Link>
                            </>
                }
            </div>
            {
                getAuthInformation('token') &&
                    <div className='event-statistics-container'>
                        <div className='event-statistics-chart'>
                            {
                                getCurrentEventStatisticsToDisplay() && getCurrentEventStatisticsToDisplay().averages &&
                                <Bar
                                    options={setChartOptions('Averages')}
                                    data={getCurrentEventStatisticsToDisplay()?.averages}
                                />
                            }
                        </div>
                        <div className='event-statistics-chart'>
                            <Doughnut
                                options={setChartOptions('Number of Events By Category')} 
                                data={{
                                    labels: getCurrentEventStatisticsToDisplay()?.numberOfEventsByCategory?.labels,
                                    datasets: [{
                                        label: 'Number of Events By Category',
                                        data: getCurrentEventStatisticsToDisplay()?.numberOfEventsByCategory?.data,
                                        backgroundColor: getCurrentEventStatisticsToDisplay()?.numberOfEventsByCategory?.bgColor,
                                        borderColor: getCurrentEventStatisticsToDisplay()?.numberOfEventsByCategory?.borderColor,
                                        borderWidth: 1,
                                    }]
                                }}
                            />
                        </div>
                        <div className='event-statistics-chart'>
                            {
                                getCurrentEventStatisticsToDisplay() && getCurrentEventStatisticsToDisplay().averageCostOfEventsByCategory &&
                                    <PolarArea
                                        options={setChartOptions('Average Cost of Events By Category')} 
                                        data={getCurrentEventStatisticsToDisplay()?.averageCostOfEventsByCategory}
                                    />
                            }
                        </div>
                        <div className='event-statistics-chart'>
                            {
                                getCurrentEventStatisticsToDisplay() && getCurrentEventStatisticsToDisplay().averageAgeLimitOfEventsByCategory &&
                                    <PolarArea
                                        options={setChartOptions('Average Age Limit of Events By Category')} 
                                        data={getCurrentEventStatisticsToDisplay()?.averageAgeLimitOfEventsByCategory}
                                    />
                            }            
                        </div>
                        <div className='event-statistics-chart'>
                            <Chart
                                options={setChartOptions('Number of Events By Category within Time Ranges')} 
                                ref={chartRef}
                                type='line'
                                data={getCurrentEventStatisticsToDisplay() && getCurrentEventStatisticsToDisplay().numberOfEventsByCategoryTimeRanges
                                    ? {
                                    ...getCurrentEventStatisticsToDisplay().numberOfEventsByCategoryTimeRanges,
                                    datasets: getCurrentEventStatisticsToDisplay().numberOfEventsByCategoryTimeRanges.datasets.map(dataset => ({
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
            }
        </>
    );
};

export default EventStatistics;