import axios from 'axios';

const hint = '비밀번호';
function pretendLoginRequest(email, pass, cb) {
  axios({
    method: 'post',
    url: '/api/login',
    params: {
      email: 'test@test.co.kr',
      password: 'test'
    }
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

  // axios({
  //   method: 'post',
  //   url: '/api/login',
  //   params: {
  //     email: 'test@test.co.kr',
  //     password: 'test'
  //   }
  // }).then(result => {
  //   console.log("ok", result);
  //   axios({
  //     method: 'get',
  //     url: '/api/me'
  //   }).then(resultme => {
  //     console.log('me', resultme)
  //   }).catch(function (error) {
  //     console.log("error", error)
  //   });
  // }).catch(function (error) {
  //   console.log("error", error)
  // });
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
