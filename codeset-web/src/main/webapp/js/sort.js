/**
 * 根据某个字段进行排序
 * 使用规则 s.sort(by('first', by('last')));
 * @param name 字段名称
 * @param minor 次一级排序规则，第二个by就是次级排序比较字段
 * @returns {Function}
 */
var by = function (name, minor) {

    return function (o, p) {

        var a, b;
        if(o && p && typeof o === 'object' && typeof p === 'object'){
            a = o[name];
            b = p[name];
            if(a === b){
                return typeof minor === 'function' ? minor(o, p):0;
            }
            if(typeof a === typeof b){
                return a < b ? -1:1;
            }
            return typeof a < typeof b ? -1 : 1;
        }else{
            throw {
                name:'Error',
                message:'Expected an object when sorting by ' + name
            }
        }
    }
};




var s = [
    {'first':'yuo', 'last':'aui'},
    {'first':'auo', 'last':'zui'},
    {'first':'cuo', 'last':'yui'},
    {'first':'buo', 'last':'hui'},
    {'first':'iuo', 'last':'hui'},
    {'first':'kuo', 'last':'hui'},
    {'first':'huo', 'last':'hui'},
    {'first':'luo', 'last':'hui'},
    {'first':'ruo', 'last':'hui'},
    {'first':'guo', 'last':'hui'},
    {'first':'xuo', 'last':'hui'},
    {'first':'muo', 'last':'hui'},
    {'first':'fuo', 'last':'hui'},
    {'first':'suo', 'last':'hui'},
    {'first':'xuo', 'last':'hui'},
    {'first':'nuo', 'last':'hui'},
    {'first':'ouo', 'last':'hui'},
    {'first':'euo', 'last':'hui'},
    {'first':'xuo', 'last':'aui'},
    {'first':'quo', 'last':'hui'},
    {'first':2, 'last':'hui'},
    {'first':'8', 'last':'hui'},
    {'first':9, 'last':'hui'},
    {'first':'puo', 'last':'iui'},
    {'first':'duo', 'last':'pui'}
];

s.sort(by('first', by('last')));

console.log(s);
