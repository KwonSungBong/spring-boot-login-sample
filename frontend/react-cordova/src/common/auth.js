import axios from 'axios';

const hint = '비밀번호';
function pretendLoginRequest(email, pass, cb) {
  axios.post('http://localhost:8888/auth', {
    username: 'lynas',
    password: '123456'
  }).then(result => {
    const token = result.data.token;
    cb({
      authenticated: true,
      time: Date.now(),
      token: token
    });
  }).catch(function (error) {
    cb({
      authenticated: false,
      hint
    });
  });
}
function pretendTokenRequest(token, cb) {
  setTimeout(() => {
    if (token) {
      cb({
        authenticated: true,
        time: Date.now(),
        token
      });
    } else {
      cb({
        authenticated: false
      });
    }
  }, 300);
}

export default {
  onChangeHandlers: [],
  login(email, pass, cb) {
    pretendLoginRequest(email, pass, res => {
      if (res.authenticated) {
        localStorage.token = res.token;
        localStorage.time = res.time;
        if (cb) {
          cb(true);
        }
      } else if (cb) {
        cb(false, res.hint);
      }
    });
  },

  getToken() {
    return localStorage.token;
  },

  logout(cb) {
    delete localStorage.token;
    if (cb) {
      cb(false);
    }
  },

  // If doesn't have token or login time has passed, do not validate the token against the server.
  loggedIn(cb) {
    let authenticated;
    if (!localStorage.token || (localStorage.time <= Date.now() - (1000 * 60))) {
      authenticated = false;
    } else {
      pretendTokenRequest(localStorage.token, res => {
        cb(res.authenticated);
      });
    }
    return authenticated;
  }
};
