import axios from "axios";
import authHeader from "../../authentication/services/authHeader";

const API_URL = "http://localhost:8080/api/v1"


class ProfileService {

    fetchPhotos(page, size, username) {
        return axios.get(API_URL + "/photos/load/icons", {
            headers: {
                Authorization: authHeader().Authorization,
                "Content-Type": "application/json",
            },
            params: {
                page: page,
                size: size,
                username: username
            }
        })
            .then(response => {
                if (response.data) {
                    localStorage.setItem("profilePhotos", JSON.stringify(response.data));
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    }
}

export default new ProfileService();