import axios from "axios";
import authHeader from "../../authentication/services/authHeader";

const API_URL = "http://localhost:8080/api/v1"


class UserDataService {

    loadUserData(username) {
        return axios.get(API_URL + "/users", {
            headers: {
                Authorization: authHeader().Authorization,
                "Content-Type": "application/json",
            },
            params: {
                username: username
            }
        })
            .then(response => {
                if (response.data) {
                    localStorage.setItem("userData", JSON.stringify(response.data));
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    };
}

export default new UserDataService();