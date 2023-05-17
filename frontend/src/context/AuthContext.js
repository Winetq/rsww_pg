import { createContext, useState, useEffect } from "react";
import jwt_decode from "jwt-decode";
import { useNavigate } from "react-router-dom";
import UrlBuilder from "../components/UrlBuilder";


const AuthContext = createContext();
export default AuthContext;


export const AuthProvider = ({children}) => {

    const navigate = useNavigate();
    const urlBuilder = new UrlBuilder();

    const authTokenUrl = urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_LOGIN_URL');
    const refreshAuthTokenUrl = urlBuilder.build('REACT_APP_API_ROOT_URL', 'REACT_APP_API_TOKEN_REFRESH_URL');

    let localStorageAuthTokens = localStorage.getItem('authTokens');

    let [authTokens, setAuthTokens] = useState(() => localStorageAuthTokens ? JSON.parse(localStorageAuthTokens) : null);
    let [user, setUser] = useState(() => localStorageAuthTokens ? jwt_decode(localStorageAuthTokens) : null);
    // let [loading, setLoading] = useState(true);

    let loginUser = async (username, password) => {
        let response = await fetch(authTokenUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
        let data = await response.json();

        if(response.status === 200){
            setAuthTokens(data);
            setUser(jwt_decode(data.access));
            localStorage.setItem('authTokens', JSON.stringify(data));
        }

        return response;
    }

    let logoutUser = () => {
        setAuthTokens(null);
        setUser(null);
        localStorage.removeItem('authTokens');
    }

    let updateToken = async () => {
        let response = await fetch(refreshAuthTokenUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({refresh: authTokens?.refresh})
        });
        let data = await response.json();

        if(response.status == 200) {
            setAuthTokens(data);
            setUser(jwt_decode(data.access));
            localStorage.setItem('authTokens', JSON.stringify(data));
        } else {
            logoutUser();
            navigate('/login');
        }

        // if(loading){
        //     setLoading(false);
        // }

        return response;
    }

    let contextData = {
        loginUser: loginUser,
        logoutUser: logoutUser,
        user: user,
        authTokens: authTokens
    }

    useEffect(() => {
        // if(loading) {
        //     updateToken();
        // }

        let fourteenMinutes = 14 * 60 * 1000;

        let interval = setInterval(()=> {
            if(authTokens){
                updateToken();
            }
        }, fourteenMinutes);
        return () => clearInterval(interval)

    }, [authTokens /*, loading*/])

    return (
        <AuthContext.Provider value={contextData}>
            {children}
        </AuthContext.Provider>
    )
}