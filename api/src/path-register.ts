import { addAliases } from 'module-alias';
import { join } from 'path';

addAliases({
    '@': join(__dirname),
});
