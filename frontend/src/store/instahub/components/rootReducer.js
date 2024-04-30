import {combineReducers} from "redux";
import authenticationReducer from "./authentication/reducers/authenticationReducer";

export const rootReducer = combineReducers({
    authenticationReducer,
});
