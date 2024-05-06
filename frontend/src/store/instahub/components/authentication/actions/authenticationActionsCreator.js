import {LOGIN_FAIL, LOGIN_SUCCESS, LOGOUT, REFRESH_FAIL, REFRESH_SUCCESS} from "./authenticationActionTypes";

import authenticationService from "../services/authenticationService";

export const login = (username, password) => (dispatch) => {
    return authenticationService.login(username, password)
        .then(
            (data) => {
                dispatch({
                    type: LOGIN_SUCCESS,
                    payload: { user:data },
                });

                return Promise.resolve();
            },
            (error) => {
                error.message = error.response?.data?.message || 'error due login';
                dispatch({
                    type: LOGIN_FAIL,
                });
                return Promise.reject(error);
            }
        );
};

export const refresh = () => (dispatch) => {
    console.log('calling service')
    return authenticationService.refresh()
        .then(
            (data) => {
                dispatch({
                    type: REFRESH_SUCCESS,
                    payload: { user:data },
                });

                return Promise.resolve();
            },
            (error) => {
                error.message = error.response?.data?.message || 'error due refresh';
                dispatch({
                    type: REFRESH_FAIL,
                });

                return Promise.reject(error);
            }
        );
};

export const logout = () => (dispatch) => {
    authenticationService.logout();
    dispatch({
        type: LOGOUT,
    });
};

export const activate = (token) => (dispatch) => {
    return authenticationService.activate(token)
        .then(
            (data) => {
                dispatch({
                    type: LOGIN_SUCCESS,
                    payload: {user: data},
                });
                return Promise.resolve();
            },
            (error) => {
                error.message = error.response?.data?.message || 'error due activate';
                dispatch({
                    type: LOGIN_FAIL,
                });

                return Promise.reject(error);
            }
        );
};