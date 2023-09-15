using System.Threading.Tasks;

namespace Controller.Application.Common.Interfaces;

public interface IServerSentEventService
{
    public Task Send<T>(T data);

    public Task Send<T>(string evenType, T data);
}