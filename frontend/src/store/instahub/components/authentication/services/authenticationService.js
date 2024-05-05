import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/auth"


class AuthenticationService {

    login (username, password) {
        return axios
            .post( "/api/v1/auth/login", {username, password},{
                headers: {'Content-Type': 'application/json'},
                withCredentials: true
            })
            .then((response) => {
                if (response.data) {
                    response.data.username = username
                    localStorage.setItem("user", JSON.stringify(response.data));
                }
                return response.data;
            })
    };

    logout() {
        localStorage.removeItem("user");
    };

    activate (token) {
        return axios
            .post(API_URL+"?token="+token)
    };

    refresh() {
        return axios
            .post(API_URL + "/refresh", {},{
                headers: {'Content-Type': 'application/json'},
                withCredentials: true
            })
            .then((response) => {
                let storedUser = localStorage.getItem('user');
                if (response.data&&storedUser) {
                    response.data.username = JSON.parse(storedUser).username;
                    localStorage.setItem("user", JSON.stringify(response.data));
                }
                return response.data;
            })
    }
}

export default new AuthenticationService();