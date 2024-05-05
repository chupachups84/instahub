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
                    username: username
                }
            }),
            axios.get(API_URL + "/users/" + username + "/followers/count", {
                headers: {
                    Authorization: authHeader().Authorization,
                    "Content-Type": "application/json",
                }
            }),
            axios.get(API_URL + "/users/" + username + "/follows/count", {
                headers: {
                    Authorization: authHeader().Authorization,
                    "Content-Type": "application/json",
                }
            }),
            axios.get(API_URL + "/users/" + username + "/relation", {
                headers: {
                    Authorization: authHeader().Authorization,
                    "Content-Type": "application/json",
                },
                params: {
                    username: username
                }
            })
        ]).then(axios.spread((userDataResponse, followersCountResponse, followsCountResponse, relationResponse) => {
            if (userDataResponse.data && followersCountResponse.data && followsCountResponse.data) {
                userDataResponse.data.followersCount = followersCountResponse.data.count;
                userDataResponse.data.followsCount = followsCountResponse.data.count;
                userDataResponse.data.relation = relationResponse.data;
                let allUsers = JSON.parse(localStorage.getItem("userData")) === null ?
                    new Map() : new Map(JSON.parse(localStorage.getItem("userData")));
                allUsers.set(username, userDataResponse.data);

                localStorage.setItem("userData", JSON.stringify(Array.from(allUsers)));
            }
            return userDataResponse.data;
        })).catch(err => {
            console.log(err);
        });
    };
}

export default new UserDataService();