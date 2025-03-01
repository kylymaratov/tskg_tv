"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Cache = void 0;
const path = __importStar(require("path"));
const fs = __importStar(require("fs"));
class Cache {
    constructor() {
        this.cachePath = path.join(__dirname, '../../', 'caches');
        const bool = fs.existsSync(this.cachePath);
        if (!bool)
            fs.mkdirSync(this.cachePath, { recursive: true });
    }
    read(fileName) {
        return __awaiter(this, void 0, void 0, function* () {
            return new Promise((resolve) => {
                fileName = this.cachePath + fileName;
                fs.readFile(fileName, (err, data) => {
                    if (err)
                        console.log(`Failed read cache file: ${fileName}`);
                    resolve(data.toJSON());
                });
            });
        });
    }
    write(fileName_1) {
        return __awaiter(this, arguments, void 0, function* (fileName, data = {}) {
            return new Promise((resolve) => {
                fileName = this.cachePath + fileName;
                fs.writeFile(fileName, data, (err) => {
                    if (err)
                        console.log(`Failed create cache file ${fileName}`);
                    resolve(null);
                });
            });
        });
    }
    del(fileName) {
        return __awaiter(this, void 0, void 0, function* () {
            return new Promise((resolve) => {
                fileName = this.cachePath + fileName;
                fs.rm(fileName, (err) => {
                    if (err)
                        console.log(`Failed delete cache file ${fileName}`);
                    resolve(null);
                });
            });
        });
    }
}
exports.Cache = Cache;
