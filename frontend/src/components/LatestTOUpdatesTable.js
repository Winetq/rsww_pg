import Table from 'react-bootstrap/Table'


const exampleChanges = [
    {
        time: "2023-06-12T10:10:10.000Z",
        event: "Add new Hotel Sunrise Hotel"
    },
    {
        time: "2023-06-12T10:12:10.000Z",
        event: "Remove trip with id = 10"
    }
]


const LatestTOUpdatesTable = ({ updates }) => {

    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th className="w-25">Time Stamp</th>
                    <th className="w-75">Update</th>
                </tr>
            </thead>
            <tbody>
                {updates.map((update) => (
                    <tr>
                        <td>{update.time}</td>
                        <td>{update.event}</td>
                    </tr>
                ))}
            </tbody>
        </Table>
    )
}

export default LatestTOUpdatesTable;