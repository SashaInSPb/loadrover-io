var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "12",
        "ok": "0",
        "ko": "12"
    },
    "minResponseTime": {
        "total": "10",
        "ok": "-",
        "ko": "10"
    },
    "maxResponseTime": {
        "total": "38",
        "ok": "-",
        "ko": "38"
    },
    "meanResponseTime": {
        "total": "18",
        "ok": "-",
        "ko": "18"
    },
    "standardDeviation": {
        "total": "10",
        "ok": "-",
        "ko": "10"
    },
    "percentiles1": {
        "total": "13",
        "ok": "-",
        "ko": "13"
    },
    "percentiles2": {
        "total": "20",
        "ok": "-",
        "ko": "20"
    },
    "percentiles3": {
        "total": "36",
        "ok": "-",
        "ko": "36"
    },
    "percentiles4": {
        "total": "38",
        "ok": "-",
        "ko": "38"
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
    "count": 12,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "6",
        "ok": "-",
        "ko": "6"
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
        "total": "9",
        "ok": "0",
        "ko": "9"
    },
    "minResponseTime": {
        "total": "12",
        "ok": "-",
        "ko": "12"
    },
    "maxResponseTime": {
        "total": "38",
        "ok": "-",
        "ko": "38"
    },
    "meanResponseTime": {
        "total": "20",
        "ok": "-",
        "ko": "20"
    },
    "standardDeviation": {
        "total": "11",
        "ok": "-",
        "ko": "11"
    },
    "percentiles1": {
        "total": "13",
        "ok": "-",
        "ko": "13"
    },
    "percentiles2": {
        "total": "34",
        "ok": "-",
        "ko": "34"
    },
    "percentiles3": {
        "total": "36",
        "ok": "-",
        "ko": "36"
    },
    "percentiles4": {
        "total": "38",
        "ok": "-",
        "ko": "38"
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
    "count": 9,
    "percentage": 100
},
    "meanNumberOfRequestsPerSecond": {
        "total": "4.5",
        "ok": "-",
        "ko": "4.5"
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
        "total": "3",
        "ok": "0",
        "ko": "3"
    },
    "minResponseTime": {
        "total": "10",
        "ok": "-",
        "ko": "10"
    },
    "maxResponseTime": {
        "total": "13",
        "ok": "-",
        "ko": "13"
    },
    "meanResponseTime": {
        "total": "11",
        "ok": "-",
        "ko": "11"
    },
    "standardDeviation": {
        "total": "1",
        "ok": "-",
        "ko": "1"
    },
    "percentiles1": {
        "total": "10",
        "ok": "-",
        "ko": "10"
    },
    "percentiles2": {
        "total": "12",
        "ok": "-",
        "ko": "12"
    },
    "percentiles3": {
        "total": "13",
        "ok": "-",
        "ko": "13"
    },
    "percentiles4": {
        "total": "13",
        "ok": "-",
        "ko": "13"
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
