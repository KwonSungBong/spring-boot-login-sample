import { take, call, put, fork, race, takeEvery } from 'redux-saga/effects'
import{ delay } from 'redux-saga'
import axios from 'axios';

function* login() {
  let request = yield take("LOGIN");
  //axios.get('http://localhost:8888/auth/test').then(data => console.log(data));

  axios.post('http://localhost:8888/auth', {
    username: 'lynas',
    password: '123456'
  }).then(result => {
    const token = result.data.token;
    axios.defaults.headers.common['X-Auth-Token'] = token;
    axios.get('http://localhost:8888/protected').then(result2 => console.log(result2))
  });
}

export default function* rootSaga() {
  yield fork(login);
}
