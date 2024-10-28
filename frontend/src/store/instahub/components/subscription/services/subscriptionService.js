import authHeader from "../../authentication/services/authHeader";
import axios from "axios";

class SubscriptionService {
    fetchFollowers(page, size, username) {
        return axios.get("api/v1/users/" + username + "/followers", {
            withCredentials: true,
            headers: authHeader().Authorization,
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
        })
    };

    fetchFollows(page, size, username) {
        return axios.get("api/v1/users/" + username + "/follows", {
            withCredentials: true,
            headers: authHeader().Authorization,
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
        })
    };
}

export default new SubscriptionService();