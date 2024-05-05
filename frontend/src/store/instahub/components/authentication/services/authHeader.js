export default function authHeader() {

    const user = JSON.parse(localStorage.getItem('user'));
    return user&&user.access_token? {Authorization: 'Bearer ' + user.access_token}:{};
}
