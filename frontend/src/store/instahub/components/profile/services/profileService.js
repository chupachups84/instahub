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
                    let storedPhotos = JSON.parse(localStorage.getItem("profilePhotos"));
                    if (storedPhotos === null || page === 0) {
                        storedPhotos = response.data
                    } else {
                        for (const v in response.data) {
                            storedPhotos.push(response.data[v]);
                        }
                    }
                    localStorage.setItem("profilePhotos", JSON.stringify(storedPhotos));
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    }

    fetchAvatar(username) {

        return axios.get("http://localhost:8080/api/v1/photos/load/avatar", {
            headers: {
                Authorization: authHeader().Authorization,
                "Content-type": "application/json",
            },
            params: {
                username: username,
            }
        })
            .then(response => {
                if (response.data) {
                    let storedAvatar = JSON.parse(localStorage.getItem("profileAvatar"));
                    if (storedAvatar === null) {
                        storedAvatar = response.data
                    } else {
                        storedAvatar.push(response.data);
                    }
                    localStorage.setItem("profileAvatar", JSON.stringify(storedAvatar));
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    }

}

export default new ProfileService();