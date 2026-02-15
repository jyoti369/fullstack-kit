/**
 * Structured Logger — Production-ready logging utility
 * Features: log levels, timestamps, context, JSON output
 */

const LOG_LEVELS = { debug: 0, info: 1, warn: 2, error: 3 };

class Logger {
  constructor(options = {}) {
    this.level = LOG_LEVELS[options.level || 'info'];
    this.context = options.context || {};
    this.pretty = options.pretty || process.env.NODE_ENV !== 'production';
  }

  _log(level, message, meta = {}) {
    if (LOG_LEVELS[level] < this.level) return;

    const entry = {
      timestamp: new Date().toISOString(),
      level: level.toUpperCase(),
      message,
      ...this.context,
      ...meta,
    };

    if (meta.error instanceof Error) {
      entry.error = {
        name: meta.error.name,
        message: meta.error.message,
        stack: meta.error.stack,
      };
    }

    const output = this.pretty
      ? JSON.stringify(entry, null, 2)
      : JSON.stringify(entry);

    if (level === 'error') {
      console.error(output);
    } else if (level === 'warn') {
      console.warn(output);
    } else {
      console.log(output);
    }
  }

  debug(msg, meta) { this._log('debug', msg, meta); }
  info(msg, meta)  { this._log('info', msg, meta); }
  warn(msg, meta)  { this._log('warn', msg, meta); }
  error(msg, meta) { this._log('error', msg, meta); }

  child(context) {
    return new Logger({
      level: Object.keys(LOG_LEVELS).find(k => LOG_LEVELS[k] === this.level),
      context: { ...this.context, ...context },
      pretty: this.pretty,
    });
  }
}

// --- Usage ---
const logger = new Logger({ level: 'debug', context: { service: 'api' } });
logger.info('Server started', { port: 3000 });

const reqLogger = logger.child({ requestId: 'abc-123' });
reqLogger.info('Processing request', { path: '/users' });
reqLogger.error('Failed', { error: new Error('DB timeout') });

module.exports = { Logger };
