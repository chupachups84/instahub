import axios from "axios";
import authHeader from "../../authentication/services/authHeader";

const API_URL = "http://localhost:8080/api/v1"


class UserDataService {

    loadUserData(username) {
        return axios.all([
            axios.get(API_URL + "/users", {
                headers: {
                    Authorization: authHeader().Authorization,
                    "Content-Type": "application/json",
                },
                params: {
                    username: username.username
                }
            }),
            axios.get(API_URL + "/users/" + username.username + "/followers/count", {
                headers: {
                    Authorization: authHeader().Authorization,
                    "Content-Type": "application/json",
                }
            }),
            axios.get(API_URL + "/users/" + username.username + "/follows/count", {
                headers: {
                    Authorization: authHeader().Authorization,
                    "Content-Type": "application/json",
                }
            })
        ]).then(axios.spread((userDataResponse, followersCountResponse, followsCountResponse) => {
            if (userDataResponse.data && followersCountResponse.data && followsCountResponse.data) {
                userDataResponse.data.followersCount = followersCountResponse.data.count;
                userDataResponse.data.followsCount = followsCountResponse.data.count;
                localStorage.setItem("userData", JSON.stringify(userDataResponse.data));
            }
            return userDataResponse.data;
        })).catch(err => {
            console.log(err);
        });
    };
}

export default new UserDataService();