import axios from "axios";
import authHeader from "../../authentication/services/authHeader";


class UserDataService {

    loadUserData(username) {
        return axios.all([
            axios.get("api/v1/users", {
                params: {
                    username: username.username
                },
                withCredentials: true,
                headers: authHeader().Authorization
            }),
            axios.get("api/v1/users/" + username.username + "/followers/count", {
                withCredentials: true,
                headers: authHeader().Authorization
            }),
            axios.get( "api/v1/users/" + username.username + "/follows/count", {
                withCredentials: true,
                headers: authHeader().Authorization
            })
        ]).then(axios.spread((userDataResponse, followersCountResponse, followsCountResponse) => {
            if (userDataResponse.data && followersCountResponse.data && followsCountResponse.data) {
                userDataResponse.data.followersCount = followersCountResponse.data.count;
                userDataResponse.data.followsCount = followsCountResponse.data.count;
                localStorage.setItem("userData", JSON.stringify(userDataResponse.data));
            }
            return userDataResponse.data;
        })).catch(err => {
           console.log(err)
        });
    };
}

export default new UserDataService();