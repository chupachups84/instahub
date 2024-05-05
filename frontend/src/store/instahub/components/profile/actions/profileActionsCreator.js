import {
    FETCH_AVATAR_FAILED,
    FETCH_AVATAR_SUCCEEDED,
    FETCH_PHOTOS_FAILED,
    FETCH_PHOTOS_SUCCEEDED,
} from "./profileActionTypes";

import profileService from "../services/profileService";

export const fetchPhotos = (page, size, username) => (dispatch) => {

    return profileService.fetchPhotos(page, size, username)
        .then(
            (data) => {
                dispatch({
                    type: FETCH_PHOTOS_SUCCEEDED,
                    payload: { profilePhotos:data },
                });
                return Promise.resolve();
            },
            (error) => {
                dispatch({
                    type: FETCH_PHOTOS_FAILED,
                    payload: error
                });

                return Promise.reject();
            }
        );
};

export const fetchAvatar = (username) => (dispatch) => {

    return profileService.fetchAvatar(username)
        .then(
            (data) => {
                dispatch({
                    type: FETCH_AVATAR_SUCCEEDED,
                    payload: { profileAvatar:data },
                });
                return Promise.resolve();
            },
            (error) => {
                dispatch({
                    type: FETCH_AVATAR_FAILED,
                    payload: error
                });

                return Promise.reject();
            }
        )

}