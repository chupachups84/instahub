import axios from "axios";


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
            .post("/api/v1/auth?token="+token)
    };

    refresh() {
        console.log('calling axios')
        return axios
            .post( "/api/v1/auth/refresh", {},{
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