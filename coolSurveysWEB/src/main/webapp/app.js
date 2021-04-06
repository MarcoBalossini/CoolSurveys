let app = new Vue ({
    el : "#vueApp",
    data: {
        username: '',
        password: '',
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
    computed: {

    },
    methods:{
        charCount: function(index){
        this.totalCharacters = this.answers[index].length;
        },
        resetFields: function() {
            this.username = '';
            this.password = '';
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
        }
    }
});