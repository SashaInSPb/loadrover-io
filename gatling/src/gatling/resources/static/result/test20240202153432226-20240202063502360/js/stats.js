var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "4",
        "ok": "0",
        "ko": "4"
    },
    "minResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "maxResponseTime": {
        "total": "114",
        "ok": "-",
        "ko": "114"
    },
    "meanResponseTime": {
        "total": "47",
        "ok": "-",
        "ko": "47"
    },
    "standardDeviation": {
        "total": "41",
        "ok": "-",
        "ko": "41"
    },
    "percentiles1": {
        "total": "30",
        "ok": "-",
        "ko": "30"
    },
    "percentiles2": {
        "total": "62",
        "ok": "-",
        "ko": "62"
    },
    "percentiles3": {
        "total": "104",
        "ok": "-",
        "ko": "104"
    },
    "percentiles4": {
        "total": "112",
        "ok": "-",
        "ko": "112"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 4,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    }
},
contents: {
"req_request-post-0-1553518593": {
        type: "REQUEST",
        name: "request_POST_0",
path: "request_POST_0",
pathFormatted: "req_request-post-0-1553518593",
stats: {
    "name": "request_POST_0",
    "numberOfRequests": {
        "total": "3",
        "ok": "0",
        "ko": "3"
    },
    "minResponseTime": {
        "total": "15",
        "ok": "-",
        "ko": "15"
    },
    "maxResponseTime": {
        "total": "114",
        "ok": "-",
        "ko": "114"
    },
    "meanResponseTime": {
        "total": "58",
        "ok": "-",
        "ko": "58"
    },
    "standardDeviation": {
        "total": "41",
        "ok": "-",
        "ko": "41"
    },
    "percentiles1": {
        "total": "45",
        "ok": "-",
        "ko": "45"
    },
    "percentiles2": {
        "total": "80",
        "ok": "-",
        "ko": "80"
    },
    "percentiles3": {
        "total": "107",
        "ok": "-",
        "ko": "107"
    },
    "percentiles4": {
        "total": "113",
        "ok": "-",
        "ko": "113"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 3,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "1.5",
        "ok": "-",
        "ko": "1.5"
    }
}
    },"req_request-get-0-1704073143": {
        type: "REQUEST",
        name: "request_GET_0",
path: "request_GET_0",
pathFormatted: "req_request-get-0-1704073143",
stats: {
    "name": "request_GET_0",
    "numberOfRequests": {
        "total": "1",
        "ok": "0",
        "ko": "1"
    },
    "minResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "maxResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "meanResponseTime": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "standardDeviation": {
        "total": "0",
        "ok": "-",
        "ko": "0"
    },
    "percentiles1": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "percentiles2": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "percentiles3": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "percentiles4": {
        "total": "14",
        "ok": "-",
        "ko": "14"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 1,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "0.5",
        "ok": "-",
        "ko": "0.5"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
