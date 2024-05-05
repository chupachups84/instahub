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
                    let allPhotos = JSON.parse(localStorage.getItem("profilePhotos")) === null ?
                        new Map() : new Map(JSON.parse(localStorage.getItem("profilePhotos")));
                    let storedPhotos = allPhotos.get(username);
                    if (storedPhotos === undefined || page === 0) {
                        allPhotos.set(username, response.data);
                    } else {
                        for (const v in response.data) {
                            storedPhotos.push(response.data[v]);
                        }
                    }
                    localStorage.setItem("profilePhotos", JSON.stringify(Array.from(allPhotos)));
                }
                return response.data;
            })
            .catch(err => {
                console.log(err)
            })
    }
}

export default new ProfileService();