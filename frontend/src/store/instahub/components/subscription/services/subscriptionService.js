import axios from "axios";
import authHeader from "../../authentication/services/authHeader";

const API_URL = "http://localhost:8080/api/v1"

class SubscriptionService {
    fetchFollowers(page, size,username) {
        return axios.get(API_URL + "/users/" + username + "/followers", {
            headers: {
                Authorization: authHeader().Authorization,
                "Content-Type": "application/json",
            },
            params: {
                page: page,
                size: size
            }
        }).then(response => {
            if (response.data) {
                let storedFollowers = JSON.parse(localStorage.getItem("followers"));
                if (storedFollowers === null || page === 0) {
                    storedFollowers = response.data
                } else {
                    for (const v in response.data) {
                        storedFollowers.push(response.data[v]);
                    }
                }
                localStorage.setItem("followers", JSON.stringify(storedFollowers));
            }
            return response.data;
        }).catch(err => {
            console.log(err)
        })
    };

    fetchFollows(page, size,username) {
        return axios.get(API_URL + "/users/" + username + "/follows", {
            headers: {
                Authorization: authHeader().Authorization,
                "Content-Type": "application/json",
            },
            params: {
                page: page,
                size: size
            }
        }).then(response => {
            if (response.data) {
                let storedFollows = JSON.parse(localStorage.getItem("follows"));
                if (storedFollows === null || page === 0) {
                    storedFollows = response.data
                } else {
                    for (const v in response.data) {
                        storedFollows.push(response.data[v]);
                    }
                }
                localStorage.setItem("follows", JSON.stringify(storedFollows));
            }
            return response.data;
        }).catch(err => {
            console.log(err)
        })
    };
}

export default SubscriptionService();