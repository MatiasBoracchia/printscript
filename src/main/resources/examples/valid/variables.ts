import "file";

let bool1: boolean = true;
let bool2: boolean = false;
let bool3: boolean = 2 > 1;
let bool4: boolean = 2 >= 1;
let bool5: boolean = 2 >= 2;
let bool6: boolean = 2 < 1;
let bool7: boolean = 2 <= 2;
let bool8: boolean = 2 == 2;
let bool9: boolean = 2 != 2;

let boolReassign: boolean;
boolReassign = bool1;
print(boolReassign);
boolReassign = bool2;
print(boolReassign);
boolReassign = bool3;
print(boolReassign);
boolReassign = bool4;
print(boolReassign);
boolReassign = bool5;
print(boolReassign);
boolReassign = bool6;
print(boolReassign);
boolReassign = bool7;
print(boolReassign);
boolReassign = bool8;
print(boolReassign);
boolReassign = bool9;
print(boolReassign);

let w: number = 5 * 2;
let x: number = 5 + 1;
let y: number = 5 - 1;
let z: number = 5 / 2;
z = z * 2;

let numReassign: number;
numReassign = w;
print(numReassign);
numReassign = x;
print(numReassign);
numReassign = y;
print(numReassign);
numReassign = z;
print(numReassign);

let s1: string = "Something";
let s2: string = 'AnotherThing';
let s3: string = s1 + s2 + true + "" + 1;

let strReassign: string;
strReassign = s1;
print(strReassign);
strReassign = s2;
print(strReassign);
strReassign = s3;
print(strReassign);

if (bool2) {
    print(x);
} else {
    if (bool3) {
        let b: number = 2;
        b = z;
        print(b);
    }
}
