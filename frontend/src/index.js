import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import SignUpPage from "./pages/sign-up/SignUpPage";
import reportWebVitals from './reportWebVitals';
import {createBrowserRouter, RouterProvider,} from "react-router-dom";
import HomePage from "./pages/home/HomePage";
import SignInPage from "./pages/sign-in/SignInPage";
import ProfilePage from "./pages/my-profile/ProfilePage";
import {Provider} from "react-redux";
import store, {persistor} from "./store/instahub";
import ActivateUserPage from "./pages/activate-user/ActivateUserPage";
import {PersistGate} from "redux-persist/integration/react";

const router = createBrowserRouter([
    {
        path: "/",
        element: <HomePage/>
    },
    {
        path: "/sign-up",
        element: <SignUpPage/>
    },
    {
        path: "/sign-in",
        element: <SignInPage/>
    },
    {
        path: "/activate-user",
        element: <ActivateUserPage/>
    },
    {
        path: "/:username",
        element: <ProfilePage/>
    }
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <Provider store={store}>
        <PersistGate loading={null} persistor={persistor}>
            <React.StrictMode>
                <RouterProvider router={router}/>
            </React.StrictMode>
        </PersistGate>
    </Provider>
);

reportWebVitals();
