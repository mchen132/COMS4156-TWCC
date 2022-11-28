import react, { useEffect, useState } from 'react';
import { getEventStatistics } from '../../actions/eventActions';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import { Doughnut } from 'react-chartjs-2';
import randomColor from 'randomcolor';

ChartJS.register(ArcElement, Tooltip, Legend);

const data = {
    labels: ['Social', 'Drink'],
    datasets: [
      {
        label: 'Number of Events By Category',
        data: [12, 19],
        backgroundColor: [
          randomColor({
            format: 'rgba',
            alpha: 0.2
          }),
          randomColor({
            format: 'rgba',
            alpha: 0.2
          }),
        ],
        borderColor: [
            randomColor({
                format: 'rgba',
                alpha: 1
              }),
              randomColor({
                format: 'rgba',
                alpha: 1
              }),
        ],
        borderWidth: 1,
      },
    ],
  };

const EventStatistics = () => {
    const [localEventStatistics, setLocalEventStatistics] = useState({
        numberOfEventsByCategory: { data: [], labels: [], bgColor: [], borderColor: [] }
    });

    useEffect(() => {
        const fetchEventStatistics = async () => {
            try {
                const eventStatistics = await getEventStatistics();

                console.log(eventStatistics);
                const numberOfEventsByCategoryColors = Object.keys(eventStatistics.numberOfEventsByCategory).map(x => randomColor({ format: 'rgb' }));

                setLocalEventStatistics({
                    numberOfEventsByCategory: {
                        data: Object.keys(eventStatistics.numberOfEventsByCategory).map(x => eventStatistics.numberOfEventsByCategory[x]),
                        labels: Object.keys(eventStatistics.numberOfEventsByCategory),
                        bgColor: numberOfEventsByCategoryColors.map(color => color.substring(0, color.length - 1) + ' 0.2)'),
                        borderColor: numberOfEventsByCategoryColors.map(color => color.substring(0, color.length - 1) + ' 1)')
                    }
                })
            } catch (err) {
                console.error(err);
            }
        };

        fetchEventStatistics();
    }, []);

    return (
        <>
            <h1>Event Statistics</h1>
            <Doughnut data={{
                    labels: localEventStatistics?.numberOfEventsByCategory?.labels,
                    datasets: [{
                        label: 'Number of Events By Category',
                        data: localEventStatistics?.numberOfEventsByCategory?.data,
                        backgroundColor: localEventStatistics && localEventStatistics?.numberOfEventsByCategory?.bgColor,
                        borderColor: localEventStatistics && localEventStatistics?.numberOfEventsByCategory?.borderColor,
                        borderWidth: 1,
                    }]
                }}
            />
        </>
    );
};

export default EventStatistics;