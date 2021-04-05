let app = new Vue ({
    el : "#vueApp",
    data: {
        username: '',
        password: '',
        email: '',
        answer: '',
        questions: ['Do you like the daily product?'],
        options: ['yes', 'no'],
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
        leaderboard:false
    },
    computed: {

    },
    methods:{
        resetFields: function() {
            this.username = '';
            this.password = '';
            this.email = '';
            this.answer= '';
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
        }
    }
});