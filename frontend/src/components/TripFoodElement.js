
const TripFoodElement = ({food, setFood}) => {
    let handleChange = (event) => {
        setFood(food);
    }

    return (
        <div className="me-2">
            <input 
                type="radio" 
                className="btn-check" 
                name="food-options" 
                id={"food-option-"+food} 
                autoComplete="off" 
                onChange={handleChange}
            />
            <label className="btn btn-outline-secondary w-100" htmlFor={"food-option-"+food}>
                {food}
            </label>
        </div>
        
    )
}

export default TripFoodElement;