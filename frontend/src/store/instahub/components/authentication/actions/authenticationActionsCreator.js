import {
    LOGIN_SUCCESS,
    LOGIN_FAIL,
    LOGOUT,
} from "./authenticationActionTypes";

import authenticationService from "../services/authenticationService";

export const login = (username, password) => (dispatch) => {
    return authenticationService.login(username, password)
        .then(
        (data) => {
            dispatch({
                type: LOGIN_SUCCESS,
                payload: { user:data },
            });
            console.log(Promise.resolve());
            return Promise.resolve();
        },
        (error) => {

            dispatch({
                type: LOGIN_FAIL,
            });

            return Promise.reject();
        }
    );
};

export const logout = () => (dispatch) => {
    authenticationService.logout();

    dispatch({
        type: LOGOUT,
    });
};