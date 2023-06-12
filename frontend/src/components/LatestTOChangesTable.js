import Table from 'react-bootstrap/Table'


const exampleChanges = [
    {
        timeStamp: "2023-06-12T10:10:10.000Z",
        change: "Add new Hotel Sunrise Hotel"
    },
    {
        timeStamp: "2023-06-12T10:12:10.000Z",
        change: "Remove trip with id = 10"
    }
]


const LatestTOChangesTable = ({ changes }) => {
    // changes = exampleChanges;

    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th className="w-25">Time Stamp</th>
                    <th className="w-75">Change</th>
                </tr>
            </thead>
            <tbody>
                {changes.map((change) => (
                    <tr>
                        <td>{change.timeStamp}</td>
                        <td>{change.change}</td>
                    </tr>
                ))}
            </tbody>
        </Table>
    )
}

export default LatestTOChangesTable;