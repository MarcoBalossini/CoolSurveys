let app = new Vue ({
    el : "#vueApp",
    data: {
        username: '',
        password: '',
        passwordConfirm: '',
        email: '',
        answers: [],
        questions: ['Do you like the daily product?', 'Do you find it comfortable?', 'Do you think it is durable?'],
        options: [['yes', 'no'], null, [0,1]],
        age: 0,
        sex: 0,
        explvl: '',
        scores:[['Michele', 0], ['Gigi', 9] ],
        message: '',
        welcome:true,
        homepage:false,
        login:false,
        registration:false,
        section1:false,
        section2:false,
        leaderboard:false,
        totalCharacters: 0
    },

    //computed properties: dynamic data based on other dynamic data

    computed: {
        validRegForm: function () {
            return this.password === this.passwordConfirm;
        }
    },
    methods:{
        charCount: function(index){
            this.totalCharacters = this.answers[index].length;
        },
        resetAll: function() {
            this.username = '';
            this.password = '';
            this.passwordConfirm= '';
            this.email = '';
            this.answers= [];
            this.questions= [];
            this.options= null;
            this.sex = 0;
            this.age = 0;
            this.explvl = '';
            this.scores = [];
            this.message = '';
            this.welcome = true;
            this.homepage = false;
            this.login = false;
            this.registration = false;
            this.section1 = false;
            this.section2 = false;
            this.leaderboard = false;
            this.totalCharacters = 0;
        },
        resetLoginForm: function () {
            this.username='';
            this.password='';
            this.welcome = true;
            this.login = false;
        },
        resetRegistrationForm: function (){
            this.username='';
            this.password='';
            this.passwordConfirm= '';
            this.email='';
            this.welcome = true;
            this.registration = false;
        },
        resetSection1Form: function () {
            this.answers=[];
            if (this.totalCharacters !==0)
                this.totalCharacters = 0;
            this.homepage = true;
            this.section1 = false;
        },
        resetSection2Form: function () {
            this.age='';
            this.sex='';
            this.explvl='';
            this.homepage = true;
            this.section2 = false;
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
                    this.message = response.data;
                    this.email = '';
                    this.password = '';
                    this.passwordConfirm = '';
                }).catch(response => {
                    this.message = response.data;
                    this.password = '';
                    this.passwordConfirm = '';
                });
        },
        submitFormLoginRequest: function() {
            axios.post("./CheckLogin", {
                username: this.username,
                password: this.password
            }).then(response => {
                console.log(response.data)
                //To change html file:
                //window.location.href = "/nome.html";
            }).catch(response => {
                console.log(response.data)
            });
        }
    }
});