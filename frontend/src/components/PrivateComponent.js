import React, { useContext } from 'react';
import AuthContext from '../context/AuthContext';


const PrivateComponent = ({children, ...rest}) => {
    const {user} = useContext(AuthContext);
    return user ? children : <></>;
}

export default PrivateComponent;