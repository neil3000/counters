import path from 'path';
import { fileURLToPath } from 'url';
import { FlatCompat } from '@eslint/eslintrc';
import pluginJs from '@eslint/js';
import eslintConfigPrettier from 'eslint-config-prettier';
import eslintConfigGoogle from 'eslint-config-google';

// mimic CommonJS variables -- not needed if using CommonJS
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
	baseDirectory: __dirname,
	recommendedConfig: pluginJs.configs.recommended
});

export default [
	{
		ignores: [
			'.DS_Store',
			'**/node_modules/',
			'**/build/',
			'.env',
			'.env.*',
			'!.env.example',
			'pnpm-lock.yaml'
		]
	},
	{
		languageOptions: {
			parserOptions: {
				sourceType: 'module',
				ecmaVersion: 2020
			}
		}
	},
	...compat.extends('standard'),
	eslintConfigGoogle,
	eslintConfigPrettier
];
