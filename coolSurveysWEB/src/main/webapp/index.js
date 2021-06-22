let index = new Vue ({
    el : "#indexApp",
    data: {
        username: '',
        password: '',
        passwordConfirm: '',
        email: '',
        message: '',
        welcome:true,
        login:false,
        registration:false
    },

    //computed properties: dynamic data based on other dynamic data

    computed: {
        validRegForm: function () {
            return this.password === this.passwordConfirm;
        }
    },
    methods:{
        resetAll: function() {
            this.username = '';
            this.password = '';
            this.passwordConfirm= '';
            this.email = '';
            this.message = '';
            this.welcome = true;
            this.homepage = false;
            this.login = false;
            this.registration = false;
        },
        resetLoginForm: function () {
            this.username='';
            this.password='';
            this.welcome = true;
            this.login = false;
            this.message = '';
        },
        resetRegistrationForm: function (){
            this.username='';
            this.password='';
            this.passwordConfirm= '';
            this.email='';
            this.welcome = true;
            this.registration = false;
            this.message = '';
        },
        resetSection1Form: function () {
            this.answers=[];
            if (this.totalCharacters !==0)
                this.totalCharacters = 0;
            this.homepage = true;
            this.section1 = false;
            this.message = '';
        },
        resetSection2Form: function () {
            this.age='';
            this.sex='';
            this.explvl='';
            this.homepage = true;
            this.section2 = false;
            this.message = '';
        },
        submitRegisterRequest: function() {
            axios.post("./DoRegistration", {
                    email: this.email,
                    username: this.username,
                    password: this.password,
                    passwordConfirm: this.passwordConfirm
                }).then(response => {
                    this.login = true;
                    this.registration = false;
                    this.message = response.response.data;
                    this.email = '';
                    this.password = '';
                    this.passwordConfirm = '';
                }).catch(response => {
                    this.message = response.response.data;
                    this.password = '';
                    this.passwordConfirm = '';
                });
        },
        submitFormLoginRequest: function() {
            axios.post("./CheckLogin", {
                username: this.username,
                password: this.password
            }).then(response => {
                this.login = false;
                this.homepage = true;
                console.log(response.response.data);
                window.location.href = "userHome.html";
            }).catch(response => {
                this.message = response.response.data;
                console.log(response.response.data)
            });
        },
        advanceSurveySection: function() {
            this.section1 = false;
            this.section2 = true;
        }
    }
});